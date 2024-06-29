method differences(arr: seq<int>) returns (diffs: seq<int>)
  requires |arr| > 0
  ensures |diffs| == |arr|-1
  ensures forall k: int :: 0 <= k < |diffs| ==> diffs[k] == arr[k+1] - arr[k]
{
  diffs := [];
  var a := 0;
  while a < |arr|-1 
    invariant a <= |arr|-1 && |diffs| == a && forall n: int :: 0 <= n < a ==> diffs[n] == arr[n+1] - arr[n]
    decreases |arr|-1-a
  {
    diffs := diffs + [arr[a+1] - arr[a]];
    a := a+1;
  }
}