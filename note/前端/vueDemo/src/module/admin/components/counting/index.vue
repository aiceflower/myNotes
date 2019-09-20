<template>
   <div class="com-counting" :style="style">
     <div class="hour">{{hour}}</div>
     <div class="detail">
       <div class="date">{{date}}</div>
       <div class="week">{{weekDay}}</div>
     </div>
   </div>
</template>

<script type="text/ecmascript-6">
  import { getDateObj, getZhWeekDay } from '../../../../common/utils/date.utils.js'
  import { prefixTimeZero } from '../../../../common/utils/number.utils.js'

  let timer = null;
  export default {
    props : {
      top : {
        type : Number,
        default : 33
      }
    },
    data : function(){
      return {
        time : (new Date).getTime()
      };
    },
    computed : {
      style : function(){
        return {
           top : this.top + 'px'
        }
      },
      hour : function(){
        let timeObj = getDateObj( this.time );

        return [ prefixTimeZero(timeObj.hour), prefixTimeZero(timeObj.minute) ].join(':')
      },
      date : function(){
        let timeObj = getDateObj( this.time );
        return [ timeObj.year, prefixTimeZero(timeObj.month), prefixTimeZero(timeObj.day)].join('.')
      },
      weekDay : function(){
        return getZhWeekDay( this.time );
      }
    },
    mounted : function(){
      timer = window.setInterval(()=>{
        this.time = (new Date).getTime();
      },1000)
    },
    destroyed : function(){
      window.clearInterval( timer )
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss"  >
  @import "./counting.scss";
</style>
