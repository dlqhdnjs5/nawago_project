<template>

	<v-container grid-list-md text-xs-center>
		
		<router-link to="/">Home</router-link> |
		<router-link to="/HelloWorld2">HelloWorld2</router-link> |
			
		<router-view>
		
		</router-view>
	
		
	</v-container>
	
</template>

<script>
export default {
  name: 'MainPage',
  data () {
    return {
    }
  },
  created: function( ){
	  
  },
  methods : {
	  goLogin : function(){
		  location.href="/login";
	  }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
