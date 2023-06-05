# Used for completion prompts

EXAMPLE_PROMPT_MULTI_FUNCTIONS = """
# There exists an example buggy function in the example project. Provide the correponding fix for the buggy function.

# Buggy Functions
## Buggy Function 1
int binarySearch(int arr[], int l, int r, int x)
{
    if (r >= l) {
        int mid = l + (r + l) / 2;
        if (arr[mid] == x)
            return mid;
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
        return binarySearch(arr, mid + 1, r, x);
    }
    return -1;
}

# Fixed Functions
## Fixed Function 1
int binarySearch(int arr[], int l, int r, int x)
{
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (arr[mid] == x)
            return mid;
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
        return binarySearch(arr, mid + 1, r, x);
    }
    return -1;
}


"""

EXAMPLE_PROMPT_SINGLE_FUNCTION = """
# There exists an example buggy function in the example project. Provide the correponding fix for the buggy function.

# Buggy Function
int binarySearch(int arr[], int l, int r, int x)
{
    if (r >= l) {
        int mid = l + (r + l) / 2;
        if (arr[mid] == x)
            return mid;
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
        return binarySearch(arr, mid + 1, r, x);
    }
    return -1;
}

# Fixed Function
int binarySearch(int arr[], int l, int r, int x)
{
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (arr[mid] == x)
            return mid;
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
        return binarySearch(arr, mid + 1, r, x);
    }
    return -1;
}


"""
