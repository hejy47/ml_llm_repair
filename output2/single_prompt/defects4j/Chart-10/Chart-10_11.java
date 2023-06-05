## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    if(toolTipText == null || toolTipText.isEmpty()){
        return "";
    }
    return " title=\"" + toolTipText + "\" alt=\"\"";
}