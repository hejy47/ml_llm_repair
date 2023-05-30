## Fixed Function 1
public int compareTo(Object other) {
    if(iValue == ((ValuedEnum) other).iValue){
        return 0;
    }
    else if(iValue > ((ValuedEnum) other).iValue){
        return 1;
    }
    else{
        return -1;
    }
}