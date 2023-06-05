@Override
public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 1; // fix: return 1 to indicate one parameter has been processed
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, param);
        }
        return 1;
    }
}