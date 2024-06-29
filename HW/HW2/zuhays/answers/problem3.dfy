method differences(arr: array?<int>) returns (diffs: array<int>)
  requires arr != null && arr.Length > 0
  ensures diffs.Length == arr.Length-1
  ensures forall k: int :: 0 <= k < diffs.Length ==> diffs[k] == arr[k+1] - arr[k]
{
  diffs := new int[arr.Length-1];
  var a := 0;
  while a < diffs.Length
    invariant a <= diffs.Length && diffs.Length == arr.Length-1 && forall n: int :: 0 <= n < a ==> diffs[n] == arr[n+1] - arr[n]
    decreases diffs.Length - a
  {
    diffs[a] := arr[a+1] - arr[a];
    a := a+1;
  }
  return diffs;
}