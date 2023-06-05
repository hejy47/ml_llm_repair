## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    return " title=\"" + toolTipText.replace("\"", "&quot;") + "\" alt=\"\"";
}