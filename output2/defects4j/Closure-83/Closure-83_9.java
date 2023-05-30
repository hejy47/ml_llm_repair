## Fixed Function 1
@Override
public int parseArguments(Parameters params) throws CmdLineException {
    if (params.size() == 0) {
        setter.addValue(true);
        return 0;
    } else {
        String param = params.getParameter(0).toLowerCase();
        if (TRUES.contains(param)) {
            setter.addValue(true);
        } else if (FALSES.contains(param)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException(owner, new LocalizableError(Messages.ILLEGAL_BOOLEAN_ARGUMENT, param));
        }
        return 1;
    }
}