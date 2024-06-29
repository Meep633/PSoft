method sumn(n: int) returns (t: int)
  requires n >= 0
  ensures t == n*(n+1)/2
{
  assert !(0 < 0);
  var i := 0;
  t := 0;
  while i < n 
    invariant i <= n && t == i*(i+1)/2
    decreases n-i
  {
    i := i + 1;
    t := t + i;
  }
}

// method Main() {
//   var a := sumn(0);
//   var b := sumn(10);
  
//   assert a == 0;
//   assert b == 55;
// }