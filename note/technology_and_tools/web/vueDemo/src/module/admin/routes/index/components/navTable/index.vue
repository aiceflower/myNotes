<template>
  <div class="index-nav-table">
    <div class="col col1">
      <div class="cell">
       <div class="content">
         <a href="#">
           <div class="icon-content">
             <img src="./img/icon_shoppingmall.png" alt=""/>
           </div>
           <div class="text">明珠商城</div>
         </a>
       </div>
      </div>
      <div class="cell">
        <div class="content">
          <a href="#">
            <div class="icon-content">
              <img src="./img/icon_airline.png" alt=""/>
            </div>
            <div class="text">动态航线</div>
          </a>
        </div>
      </div>
      <div class="cell">
        <div class="content">
          <a href="#">
            <div class="icon-content">
              <img src="./img/icon_member.png" alt=""/>
            </div>
            <div class="text">会员权益</div>
          </a>
        </div>
      </div>
    </div>
    <div class="col col2">
      <div class="cell ">
        <div class="content shadow">
          <a href="#" class="href">
            <img class="bg-img" src="./img/pic_4.png" alt=""/>
            <div class="intro-text">
              <div class="tips-content">
                <img class="icon-offset icon-offset1" src="./img/icon_video.png" alt=""/>
                <span class="tips">宣传片</span>
              </div>
            </div>
          </a>
        </div>
      </div>
    </div>
    <div class="col col3">
      <div class="cell">
        <div class="content  shadow">
          <a href="#" class="href">
            <img class="bg-img" src="./img/pic_2.png" alt=""/>
            <div class="intro-text">
              <div class="tips-content">
                <img class="icon-offset icon-offset1" src="./img/icon_plane.png" alt=""/>
                <span class="tips">机型介绍</span>
              </div>
            </div>
          </a>
        </div>
      </div>
      <div class="cell">
        <div class="content shadow">
          <a href="#" class="href">
            <img class="bg-img" src="./img/pic_3.png" alt=""/>
            <div class="intro-text">
              <div class="tips-content">
                <img class="icon-offset icon-offset1" src="./img/icon-food.png" alt=""/>
                <span class="tips">座椅及餐食</span>
              </div>
            </div>
          </a>
        </div>
      </div>
    </div>
    <div class="col col4">
      <div class="cell">
        <div class="content shadow">
          <simpleImgLoop :movieList="list" @click="simpleImgLoopClick"></simpleImgLoop>
          <div class="intro-text">
            <div class="tips-content">
              <img class="icon-offset icon-offset1" src="./img/icon_movie.png" alt=""/>
              <span class="tips">空中影院</span>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script type="text/ecmascript-6">
  import simpleImgLoop from '../simpleImgLoop'

  let img = require('./img/pic_2.png');


  export default {
    components : {
      simpleImgLoop : simpleImgLoop
    },
    methods : {
      simpleImgLoopClick : function( data ){
        console.log( data );
      }
    },
    data : function(){
      return {
        list : [
          {
            poster : img,
            index : 0
          },
          {
            poster : img,
            index : 1
          }
        ]
      };
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss"  >
  @import './navTable.scss';
</style>
