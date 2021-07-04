<template>
  <div class="index-page page-container" @click="full()">
    <index-header></index-header>
    <counting></counting>
    <nav-table></nav-table>
  </div>
</template>

<script type="text/ecmascript-6">
  import indexHeader from './components/header'
  import navTable from './components/navTable'
  import counting from '../../components/counting'
  import { fullScreen, isFullScreen } from '../../../../common/utils/BOM.utils.js'

  export default {
    components : {
      indexHeader : indexHeader,
      navTable : navTable,
      counting : counting
    },
    methods : {
      full : function(){
        !isFullScreen() && fullScreen();
      }
    }
  }
</script>

<style>
  .index-page{
    background-image: url("./img/bg.jpg");
    background-size:100%;
    background-repeat: no-repeat;
  }
</style>
