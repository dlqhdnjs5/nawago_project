const path = require('path')

module.exports = {
//	ouputDir : path.resolve(__dirname,"../"+"main/resource/static"),
	outputDir: "../src/main/resources/static",  
	indexPath: "../static/index.html", 
	devServer : {
		"proxy": {
			"/api": {
				"target": "http://localhost:7011",
				"ws": true,
				"chageOrigin": true
			}
		},
		overlay: false
	},
	lintOnSave: false,
 
  "transpileDependencies": [
    "vuetify"
  ]
}