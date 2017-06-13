package de.uniorg.ui5helper.ui5;

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
        } else  if (metadata.getAggregations().containsKey(memberName)) {
            return metadata.getAggregations().get(memberName);
        } else if (metadata.getEvents().containsKey(memberName)) {
            return metadata.getEvents().get(memberName);
        }

        if (((ClassDocumentation) doc).getInherits() != null) {
            return getMetadataMember(apiIndex, ((ClassDocumentation) doc).getInherits(), memberName);
        }

        return null;
    }
}
