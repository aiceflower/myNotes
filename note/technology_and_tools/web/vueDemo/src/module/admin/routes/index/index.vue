<template>
  <div class="index-page page-container">
    <table class="flight-list-table" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th>编号</th>
          <th colspan="6">操作</th>
        </tr>
      </thead>
      <tbody>
         <tr v-for="item in flightList">
           <td class="flight-type">{{item.flightType}}</td>
           <td>上移</td>
           <td>下移</td>
           <td>删除</td>
           <td>编辑</td>
           <td>插入(在上方)</td>
           <td>插入(在下方)</td>
         </tr>
      </tbody>
    </table>
  </div>
</template>

<script type="text/ecmascript-6">
  import { post, getApi } from '../../../../common/utils/ajax.utils.js'
  import * as apis from '../../../../common/path/apis.js'

  export default {
    components : {

    },
    data : function(){
      return {
        flightList : []
      };
    },
    methods : {
      getFlightList : function(){
        post({
          url : getApi( apis.GET_FLIGHT_LIST )
        }).then(({data})=>{
          if( data.flightList && data.flightList.length ){
            this.flightList = data.flightList;
          }
        }).catch((e)=>{
          alert('获取机型列表数据异常');
          console.error(e);
        });
      }
    },

    mounted : function(){
      this.getFlightList();
    }
  }
</script>

<style lang="scss">
  @import "index.scss";
</style>
