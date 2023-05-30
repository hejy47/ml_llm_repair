## Fixed Function 1
@Override
public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 1; // Fix: should return 1 when there is no parameter
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException(owner, new Localizable() {
                @Override
                public String formatWithLocale(Locale locale, Object... args) {
                    return "Invalid parameter: " + param;
                }
            }); // Fix: should throw an exception for invalid parameter
        }
        return 1;
    }
}