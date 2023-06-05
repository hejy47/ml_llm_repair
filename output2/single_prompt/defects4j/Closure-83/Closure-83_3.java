@Override
public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 1;
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException(owner, new Localizable() {
                public String getMessage() {
                    return "Invalid value: " + param;
                }
            });
        }
        return 1;
    }
}