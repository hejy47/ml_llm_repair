## Fixed Function 1
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
            throw new CmdLineException(owner,Messages.ILLEGAL_CHOICE,param,TRUES.toString()+","+FALSES.toString());
        }
        return 1;
    }
} 

Note: This fix includes adding a return statement after adding a value when the parameter is null, and throwing an exception instead of returning 0 when the parameter value is not recognized.