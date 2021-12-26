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

	:global(.valid-btn) {
		border-radius: 1em;
		padding: 0.5em 1.2em;
		font-weight: bold;
		background-color: #fff;
		color: #ff3e00;
	}

	:global(.valid-btn):hover {
		cursor: pointer;
		background-color: #ff3e00;
		color: #fff;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>
