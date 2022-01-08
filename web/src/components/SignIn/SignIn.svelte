<script lang="ts">
    import { push } from "svelte-spa-router";
    import { signIn } from "../../api/auth/auth-service";

    let email = "";
    let password = "";
    let error = "";
    let errorShowed = false;
    $: {
        email;
        password;
        resetErrorIfShowed();
    }

    function resetErrorIfShowed() {
        if (error.length > 0) {
            if (!errorShowed) {
                error = "";
            } else {
                errorShowed = false;
            }
        }
    }

    async function submit() {
        try {
            await signIn(email, password);
            push("/meeting");
        } catch (err) {
            error = err.message;
        }
    }
</script>

<h1>Sign In</h1>

<form on:submit|preventDefault={submit} class="sign-in-form">
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
    <div style="color: red;">{error}</div>
</form>

<style lang="scss">
    .sign-in-form {
        display: flex;
        justify-content: center;
        flex-direction: column;
        padding: 0 38%;
    }
</style>
