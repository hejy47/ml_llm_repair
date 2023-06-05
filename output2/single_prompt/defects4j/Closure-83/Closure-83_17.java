## Fixed Function 1
@Override
public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 1; // return 1 instead of 0 to represent the number of consumed parameters
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException(owner, new Localizable() {
                @Override
                public String getMessage(Locale locale) {
                    return "Invalid value: " + param;
                }
            });
        }
        return 1;
    }
}