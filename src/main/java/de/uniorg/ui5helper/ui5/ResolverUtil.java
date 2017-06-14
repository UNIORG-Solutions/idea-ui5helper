package de.uniorg.ui5helper.ui5;

import java.util.function.Function;
import java.util.stream.Stream;

public class ResolverUtil {
    public static ApiSymbol getMetadataMember(ApiIndex apiIndex, String className, String memberName) {
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
