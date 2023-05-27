## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    return " title=\"" + toolTipText.replaceAll("\"", "&quot;") + "\" alt=\"\"";
} 

# The fix for the buggy function includes replacing any double quotes with the HTML entity `&quot;` to prevent issues with the HTML attribute.