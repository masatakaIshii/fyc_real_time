<script lang="ts">
    import { push } from "svelte-spa-router";
    import { isLoggedIn } from "../../stores/use-is-logged-in";
    import { logout } from "../../api/auth/auth-service";

    let path = "/home";
    $: {
        if ($isLoggedIn) {
            path = "/"
        } else {
            path = "/home"
        }
    }

</script>

<nav>
    <div><button on:click={() => push(path)}>Music challenge</button></div>
    {#if $isLoggedIn}
        <button class="logout-btn" on:click={logout}>Logout</button>
    {/if}
</nav>
<hr />

<style lang="scss">
    nav {
        padding: 0.5em 1em 1em;
        display: flex;
        justify-content: space-between;
        align-items: center;
        button {
            margin: 0;
            background-color: #fff;
            border: none;
            color: #ff3e00;
            cursor: pointer;
        }
        button:hover {
            font-weight: bold;
        }

        .logout-btn {
            color: #999;
        }
    }
    hr {
        margin: 0 1em;
    }
</style>
