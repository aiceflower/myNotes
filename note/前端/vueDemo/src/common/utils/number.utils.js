
export function prefixTimeZero( time ){
  return time >= 10 ? time : '0' + time;
}
