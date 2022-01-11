<script lang="ts">
	import Router from "svelte-spa-router";
	import { routes } from "../src/routes";
	import NavBar from "./components/NavBar/NavBar.svelte";

</script>

<main>
	<NavBar />
	<Router {routes}/>
</main>

<style lang="scss">
	main {
		text-align: center;
		max-width: 240px;
		margin: 0 auto;
	}

	:global(.delete-btn) {
		border-radius: 1em;
		padding: 0.5em 1.2em;
		font-weight: bold;
		background-color: #fff;
		color: red;
	}
	
	:global(.delete-btn):hover {
		cursor: pointer;
		background-color: red;
		color: #fff;
	}

	:global(.update-btn) {
		border-radius: 1em;
		padding: 0.5em 1.2em;
		font-weight: bold;
		background-color: #fff;
		color: #555;
	}

	:global(.update-btn):hover {
		cursor: pointer;
		background-color: #555;
		color: #fff;
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

	:global(.form-style) {
		display: flex;
        justify-content: center;
        flex-direction: column;
        padding: 0 38%;
	}

	:global(.form-group) {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		margin-bottom: 1em;
	}

	:global(.form-btn) {
		display: flex;
		justify-content: end;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>
