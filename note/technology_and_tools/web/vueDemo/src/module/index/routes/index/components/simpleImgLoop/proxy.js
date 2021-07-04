export default {
  components : {},
  setComponent( component, id ){
    if( id ){
      this.components[ id ] = component;
    }else{
      this.components[0] = component;
    }
  },
  getCurItem( id ){
    let comp = null;
    if( id ){
      comp = this.components[id];
    }else{
      comp = this.components[0];
    }

    if( !comp ){
      return null;
    }

    let list = comp.list;
    if( list.length ){
      let index = this.getCurIndex( id );
      return list[ index ];
    }else{
      return null;
    }
  },
  getCurIndex( id ){
    let comp = null;
    if( id ){
      comp = this.components[id];
    }else{
      comp = this.components[0];
    }

    if( !comp ){
      return null;
    }

    let index = comp.index;
    let list = comp.list;
    if( list.length <= 1 ){
      return 0;
    }else{
      if( index + 1 >= list.length ){
        return list.length - 1;
      }else{
        return index;
      }
    }
  }
};
