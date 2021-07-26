export const getDateObj = function( ms ){
  if( !ms ){
    return {
      year : '',
      month : '',
      day : '',
      hour : '',
      minute : '',
      second : ''
    }
  }

  ms = window.parseInt(ms);
  let $date = new Date( ms );
  let year = $date.getFullYear();
  let month = $date.getMonth() + 1;
  let day = $date.getDate();
  let hour = $date.getHours();
  let minute = $date.getMinutes();
  let second = $date.getSeconds();

  return {
    year, month, day, hour, minute, second
  };
};

let weekDays = [
  '星期天',
  '星期一',
  '星期二',
  '星期三',
  '星期四',
  '星期五',
  '星期六'
];
export const getZhWeekDay = function( ms ){
  if( !ms ){
    return '';
  }
  ms = window.parseInt(ms);
  let $date = new Date(ms);
  let weekDay = $date.getDay();
  return weekDays[weekDay];
};
