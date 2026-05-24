document.body.appendChild(document.createElement("script")).src = "https://unpkg.com/maplibre-gl@latest/dist/maplibre-gl.js";
let links = ["https://unpkg.com/maplibre-gl@latest/dist/maplibre-gl.css"];

for (var i=0;i<links.length;i++) {
	let link = document.createElement('link');
	link.setAttribute('rel', 'stylesheet');
	link.setAttribute('href', links[i]);
	link.setAttribute('type', "text/css");
	document.getElementById('shadowhost').shadowRoot.appendChild(link);
}