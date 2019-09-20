<template>
  <div class="movie-detail page-container" >
    <counting></counting>
    <com-header text="电影详情" :height="110"></com-header>
    <div class="movie-mes">
      <div class="detail-mes">
        <div class="movie-name">{{curItem.movieName}}</div>
        <div class="mes-content">
          <div class="cutter">
            <ul class="ul-list">
              <li class="text-hidden">
                导演: {{curItem.director}}
              </li>
              <li class="text-hidden">
                演员: {{curItem.actor}}
              </li>
              <li class="text-hidden">
                类型: {{curItem.movieType}}
              </li>
              <li class="text-hidden">
                时长: {{curItem.time}}
              </li>
            </ul>
            <ul class="ul-list offset">
              <li>
                简介: {{curItem.desc}}
              </li>
            </ul>
          </div>
          <div class="player"></div>
        </div>
      </div>
      <div class="poster" :style="posterStyle">
      </div>
    </div>
    <com-loop
      @click="loopClick"
      :curIndex="movieIndex"
      :list="list"></com-loop>
  </div>
</template>

<script type="text/ecmascript-6">
  import counting from '../../components/counting'
  import comHeader from '../../components/header'
  import comLoop from './components/loop'
  import mockMovieList from '../../../../common/mock/movieList.js'

  let img = require('./img/pic_1.png');

  export default {
    components : {
      counting : counting,
      comHeader : comHeader,
      comLoop : comLoop
    },
    methods : {
      loopClick : function( data ){
        this.setMovieIndex( data.index );
      },
      setMovieIndex : function( index ){
        this.movieIndex = index;
      }
    },
    data : function(){
      return {
        poster : img,
        movieIndex : 0,
        list : mockMovieList
      };
    },
    computed : {
      posterStyle : function(){
        if( this.list && this.list.length ){
          let item = this.list[ this.movieIndex ];
          return {
            backgroundImage : 'url('+ item.poster +')'
          };
        }
      },
      curItem : function(){
        if( this.list && this.list.length ){
          return  this.list[ this.movieIndex ];;
        }
      }
    },
    created : function(){
      let movieIndex = this.$router.history.current.query.index ||  0;
      this.movieIndex = movieIndex;
    },
    mounted : function(){

    }
  }
</script>

<style lang="scss">
 @import "movieDetail.scss";
</style>
