<script lang="ts">
    import { push } from "svelte-spa-router";
</script>

<h1>Music challenge</h1>
<h2>Organize meeting</h2>
<p>You can join meeting to purpose what kind of music you want to deliver</p>

<h2>Select theme</h2>
<p>
    After the purposed themes you can select one or more to decide the theme of
    challenge
</p>

<button on:click={() => push("/sign-in")}>Sign in</button>

<style>
    h1 {
        color: #ff3e00;
        text-transform: uppercase;
        font-size: 4em;
        font-weight: 100;
    }

    button {
        border-radius: 1em;
        padding: 0.5em 1.2em;
        font-weight: bold;
        background-color: #fff;
        color: #ff3e00;
    }

    button:hover {
        cursor: pointer;
        background-color: #ff3e00;
        color: #fff;
    }
</style>
