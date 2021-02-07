<template>
	<v-navigation-drawer
       v-model="drawer"
       app
       right
     >
       <v-list dense>
        <v-list-item two-line>
          <v-list-item-avatar>
            <img src="https://randomuser.me/api/portraits/women/81.jpg">
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>Jane Smith</v-list-item-title>
            <v-list-item-subtitle>Logged In</v-list-item-subtitle>
      	  </v-list-item-content>
      </v-list-item>
         <v-list-item link>
           <v-list-item-action>
             <v-icon>mdi-home</v-icon>
           </v-list-item-action>
 
           <v-list-item-content>
             <v-list-item-title>Home</v-list-item-title>
           </v-list-item-content>
         </v-list-item>
 
         <v-list-item link>
           <v-list-item-action>
             <v-icon>mdi-email</v-icon>
           </v-list-item-action>
 
           <v-list-item-content>
             <v-list-item-title>Contact</v-list-item-title>
           </v-list-item-content>
         </v-list-item>
       </v-list>
     </v-navigation-drawer>
</template>

<script>
export default {
  name: 'Nav',
  components: {
  },
  data: () => ({
	  drawer : null
  }),
  created : function(){
	  
  }
};
</script>
