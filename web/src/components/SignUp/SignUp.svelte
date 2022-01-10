<script lang="ts">
import { push } from "svelte-spa-router";

import { subscribe } from "../../api/auth/auth-service";

    import { error } from "../../stores/use-error";

    let name = "";
    let email = "";
    let password = "";

    $: {
        name
        email
        password
        error.reset()
    }
    async function submit() {
        try {
            await subscribe(name, email, password);
            push("/sign-in")
        } catch(err) {
            error.set(err);
        }
    }
</script>

<h1>Subscribe Form</h1>

<form on:submit|preventDefault={submit} class="form-style">
    <div class="form-group">
        <label for="Name">Name :</label>
        <input id="name" type="text" bind:value={name} required />
    </div>
    <div class="form-group">
        <label for="email">Email :</label>
        <input id="email" type="email" bind:value={email} required />
    </div>
    <div class="form-group">
        <label for="password">Password : </label>
        <input id="password" type="password" bind:value={password} required />
    </div>

    <div class="form-btn">
        <button type="submit" class="valid-btn">Submit</button>
    </div>
    <div style="color: red;">{$error}</div>
</form>
