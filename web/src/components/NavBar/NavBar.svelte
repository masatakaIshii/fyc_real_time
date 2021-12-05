<script lang="ts">
    import { push } from "svelte-spa-router";
</script>

<nav>
    <div><button on:click={() => push("/")}>Music challenge</button></div>
</nav>
<hr />

<style lang="scss">
    nav {
        padding: 0 1em;
        display: flex;
        button {
            background-color: #fff;
            border: none;
            color: #ff3e00;
            cursor: pointer;
        }
        button:hover {
            font-weight: bold;
        }
    }
    hr {
        margin: 0 1em;
    }
</style>
