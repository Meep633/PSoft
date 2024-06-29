method loopysqrt(n:int) returns (root:int)
  ensures root * root == n || root == -1
{
  root := 0;
  var a := n;
  while (a > 0)
    invariant (a >= 0 && n-a == root*root) || a < 0
    decreases n - root*root
  {
    root := root + 1;
    a := a - (2 * root - 1);
  }
  if a < 0 {
    root := -1;
  }
}

// method Main() {
//   var n := 0;
//   while n <= 10 {
//     var a := loopysqrt(n*n);
//     print n*n, ": ", a*a == n*n, " (", a,"^2)\n";
//     n := n+1;
//   }

//   print "\n";
//   n := 2;
//   var a := loopysqrt(n);
//   print n, ": ", a*a == n, " (", a,"^2)\n";
// }