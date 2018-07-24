package de.uniorg.ui5helper.ui5;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ResolverUtil {

    private static final Map<ApiIndex, ResolverUtil> INSTANCES = new HashMap<>();
    private ApiIndex apiIndex;

    private ResolverUtil(ApiIndex apiIndex) {
        this.apiIndex = apiIndex;
    }

    @NotNull
    public static ResolverUtil getInstanceOf(@NotNull ApiIndex apiIndex) {
        if (!INSTANCES.containsKey(apiIndex)) {
            INSTANCES.put(apiIndex, new ResolverUtil(apiIndex));
        }

        return INSTANCES.get(apiIndex);
    }

    public static ApiSymbol getMetadataMember(@NotNull ApiIndex apiIndex, @NotNull String className, @NotNull String memberName) {
        ApiSymbol doc = apiIndex.lookup(className);
        if (doc == null || !(doc instanceof ClassDocumentation)) {
            return null;
        }

        UI5Metadata metadata = ((ClassDocumentation) doc).getUI5Metadata();
        if (metadata == null) {
            return null;
        }

        if (metadata.getProperties().containsKey(memberName)) {
            return metadata.getProperties().get(memberName);
        } else if (metadata.getAggregations().containsKey(memberName)) {
            return metadata.getAggregations().get(memberName);
        } else if (metadata.getEvents().containsKey(memberName)) {
            return metadata.getEvents().get(memberName);
        }

        if (((ClassDocumentation) doc).getInherits() != null) {
            return getMetadataMember(apiIndex, ((ClassDocumentation) doc).getInherits(), memberName);
        }

        return null;
    }

    public Map<String, AggregationDocumentation> getAllAggregations(@NotNull ClassDocumentation classDocumentation) {
        UI5Metadata metadata = classDocumentation.getUI5Metadata();
        if (metadata == null) {
            return new HashMap<>();
        }

        Map<String, AggregationDocumentation> aggregations = metadata.getAggregations();

        if (classDocumentation.getInherits() != null) {
            ApiSymbol parent = this.apiIndex.lookup(classDocumentation.getInherits());
            if (parent instanceof ClassDocumentation) {
                Map<String, AggregationDocumentation> parentAggregations = this.getAllAggregations((ClassDocumentation) parent);
                parentAggregations.forEach(aggregations::putIfAbsent);
            }
        }

        return aggregations;
    }

    public AggregationDocumentation getDefaultAggregations(@NotNull ClassDocumentation classDocumentation) {
        UI5Metadata metadata = classDocumentation.getUI5Metadata();
        if (metadata == null) {
            return null;
        }

        if (metadata.getDefaultAggregation() != null) {
            return metadata.getDefaultAggregation();
        }

        if (classDocumentation.getInherits() != null) {
            ApiSymbol parent = this.apiIndex.lookup(classDocumentation.getInherits());
            if (parent instanceof ClassDocumentation) {
                return this.getDefaultAggregations((ClassDocumentation) parent);
            }
        }

        return null;
    }

    public boolean isInstanceOf(@NotNull String className, @NotNull String interfaceName) {
        if (className.equals(interfaceName)) {
            return true;
        }

        ApiSymbol classDoc = this.apiIndex.lookup(className);
        if (classDoc == null || !(classDoc instanceof ClassDocumentation)) {
            return false;
        }

        return isInstanceOf((ClassDocumentation) classDoc, interfaceName);
    }

    public boolean isInstanceOf(@NotNull ClassDocumentation classDocumentation, @NotNull String interfaceName) {
        if (classDocumentation.getName().equals(interfaceName)) {
            return true;
        }

        if (classDocumentation.getInherits() == null) {
            return false;
        }

        if (classDocumentation.getInherits().equals(interfaceName)) {
            return true;
        }

        ClassDocumentation parent = getParent(classDocumentation);
        return parent != null && this.isInstanceOf(parent, interfaceName);

    }

    private ClassDocumentation getParent(ClassDocumentation classDocumentation) {
        if (classDocumentation.getInherits() != null) {
            return (ClassDocumentation) apiIndex.lookup(classDocumentation.getInherits());
        }

        return null;
    }

    private static ClassDocumentation getParent(ApiIndex index, ClassDocumentation classDocumentation) {
        if (classDocumentation.getInherits() != null) {
            return (ClassDocumentation) index.lookup(classDocumentation.getInherits());
        }

        return null;
    }

    public static <T> Stream<T> mapMember(ApiIndex apiIndex, ClassDocumentation classDocumentation, Function<ApiSymbol, T> map) {
        ClassDocumentation parent = getParent(apiIndex, classDocumentation);
        Stream<T> parentStream = Stream.empty();
        Stream<T> own = Stream.empty();
        if (parent != null) {
            parentStream = mapMember(apiIndex, parent, map);
        }
        if (classDocumentation.getUI5Metadata() != null) {
            UI5Metadata metadata = classDocumentation.getUI5Metadata();
            own = Stream
                    .concat(
                            Stream.concat(
                                    metadata.getProperties().values().stream(),
                                    metadata.getAggregations().values().stream()
                            ),
                            metadata.getEvents().values().stream()
                    )
                    .map(map);
        }

        return Stream.concat(parentStream, own);
    }

    public static <T> Stream<T> mapMember(ApiIndex apiIndex, String className, Function<ApiSymbol, T> map) {
        ApiSymbol classDoc = apiIndex.lookup(className);
        if (classDoc == null || !(classDoc instanceof ClassDocumentation)) {
            return Stream.empty();
        }

        return mapMember(apiIndex, (ClassDocumentation) classDoc, map);
    }
}
