<script lang="ts">
    import { push } from "svelte-spa-router";
</script>

<div>
    <h1>Music challenge</h1>
    <h2>Organize meeting</h2>
    <p>
        You can join meeting to purpose what kind of music you want to deliver
    </p>

    <h2>Select theme</h2>
    <p>
        After the purposed themes you can select one or more to decide the theme
        of challenge
    </p>

    <button class="valid-btn" on:click={() => push("/sign-in")}>Sign in</button>
    <button class="valid-btn subscribe-btn" on:click={() => push("/sign-up")}>Subscribe</button>
</div>

<style>
    h1 {
        color: #ff3e00;
        text-transform: uppercase;
        font-size: 4em;
        font-weight: 100;
    }
    .subscribe-btn {
        color: gray;
    }
    .subscribe-btn:hover {
        color: white;
        background-color: grey;
    }
</style>
