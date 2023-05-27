## Fixed Function 1
public String generateToolTipFragment(String toolTipText) {
    return " title=\"" + toolTipText.replaceAll("\"", "&#34;") + "\" alt=\"\"";
}

The fixed function uses the replaceAll() method to replace any double quotes in the toolTipText with the corresponding HTML entity "&#34;", to prevent errors in the HTML code.