## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    if (toolTipText == null || toolTipText.isEmpty()) {
        return "";
    }
    return " title=\"" + toolTipText + "\" alt=\"\"";
} 

(This fixes the issue where a null or empty toolTipText parameter would still result in a non-empty tooltip fragment, causing issues with accessibility and a misleading tooltip display.)