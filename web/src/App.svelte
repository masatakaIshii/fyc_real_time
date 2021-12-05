<script lang="ts">
	import Router from "svelte-spa-router";
	import { routes } from "../src/routes";
	import NavBar from "./components/NavBar/NavBar.svelte";
</script>

<main>
	<NavBar />
	<Router {routes} />
</main>

<style>
	main {
		text-align: center;
		max-width: 240px;
		margin: 0 auto;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>
