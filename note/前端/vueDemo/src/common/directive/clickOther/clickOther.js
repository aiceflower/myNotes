export default {

    bind( el, binding ){
        binding.$handler = (e) => {
            if( !el.contains( e.target ) ){
                binding.value.cb( e );
            }
        };
        document.addEventListener( 'click', binding.$handler, false );
    },

    unbind( el, binding ){
        document.removeEventListener( 'click', binding.$handler, false );
    }

};