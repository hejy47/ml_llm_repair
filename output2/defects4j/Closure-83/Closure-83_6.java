## Fixed Function 1
@Override
public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 1; // changed from 0 to 1 to signal that one argument was consumed
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            setter.addValue(true);
            return 1; // changed from 0 to 1 to signal that one argument was consumed
        }
        return 1; // changed from 0 to 1 to signal that one argument was consumed
    }
}