method dutch(arr: array?<char>) returns (k: int)
  modifies arr
  requires arr != null
  requires forall n: int :: 0 <= n < arr.Length ==> arr[n] == 'r' || arr[n] == 'b'
  ensures 0 <= k <= arr.Length 
  ensures forall n: int :: 0 <= n < k ==> arr[n] == 'r' 
  ensures forall n: int :: k <= n < arr.Length ==> arr[n] == 'b'
{
  k := 0;
  var i := 0;
  while i < arr.Length 
    invariant k <= i <= arr.Length 
    invariant forall n: int :: 0 <= n < k ==> arr[n] == 'r'
    invariant forall n: int :: k <= n < i ==> arr[n] == 'b'
    invariant forall n: int :: i <= n < arr.Length ==> arr[n] == old(arr[n])
    decreases arr.Length - i
  {
    if arr[i] == 'r' {
      swap(arr, i, k);
      k := k+1;
    }
    i := i+1;
  }
  return k;
}

method swap(arr: array?<char>, i: int, j: int)
  modifies arr
  requires arr != null && 0 <= i < arr.Length && 0 <= j < arr.Length
  ensures arr[i] == old(arr[j]) && arr[j] == old(arr[i])
  ensures forall n: int :: 0 <= n < arr.Length && n != i && n != j ==> arr[n] == old(arr[n])
{
  var temp := arr[i];
  arr[i] := arr[j];
  arr[j] := temp;
}

// method Main() {
//   var arr: array?<char> := new char[]['r', 'b', 'b', 'r'];
//   var k := dutch(arr);
//   var i := 0;
//   while i < k {
//     print arr[i], " ";
//     i := i+1;
//   }
//   print "\n";
//   i := k;
//   while i < arr.Length {
//     print arr[i], " ";
//     i := i+1;
//   }
  
//   print "\n\n";

//   arr := new char[]['b', 'b', 'r'];
//   k := dutch(arr);
//   i := 0;
//   while i < k {
//     print arr[i], " ";
//     i := i+1;
//   }
//   print "\n";
//   i := k;
//   while i < arr.Length {
//     print arr[i], " ";
//     i := i+1;
//   }

//   print "\n\n";

//   arr := new char[]['b', 'b'];
//   k := dutch(arr);
//   i := 0;
//   while i < k {
//     print arr[i], " ";
//     i := i+1;
//   }
//   print "\n";
//   i := k;
//   while i < arr.Length {
//     print arr[i], " ";
//     i := i+1;
//   }

//   print "\n\n";

//   arr := new char[]['r', 'r'];
//   k := dutch(arr);
//   i := 0;
//   while i < k {
//     print arr[i], " ";
//     i := i+1;
//   }
//   print "\n";
//   i := k;
//   while i < arr.Length {
//     print arr[i], " ";
//     i := i+1;
//   }
// }