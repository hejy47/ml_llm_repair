## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    if(toolTipText != null && !toolTipText.isEmpty()){
        return " title=\"" + toolTipText + "\" alt=\"\"";
    } else {
        return "";
    }
}