<script lang="ts">
import { push } from "svelte-spa-router";

import { createMeeting } from "../../api/meeting/meeting-service";


    let name: string = "";
    let description: string = "";
    let error: string;

    async function submit() {
        await createMeeting(name, description);
        push("/");
    }
</script>

<h1>Meeting form</h1>

<form on:submit|preventDefault={submit}>
    <div class="form-group">
        <label for="name">Name :</label>
        <input id="name" type="text" bind:value={name} required />
    </div>
    <div class="form-group">
        <label for="description">Description :</label>
        <textarea id="description" bind:value={description} />
    </div>
    <div class="form-btn">
        <button type="submit" class="valid-btn">Submit</button>
    </div>
    {#if error}
        <div style="color: red;">{error}</div>
    {/if}
</form>

<style lang="scss">
    form {
        display: flex;
        justify-content: center;
        flex-direction: column;
        padding: 0 38%;
        input {
            min-width: 50%;
        }
        textarea {
            min-width: 50%;
        }
    }
</style>
