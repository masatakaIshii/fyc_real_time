<script lang="ts">
    import { push } from "svelte-spa-router";
    import { createMeeting } from "../../api/meeting/meeting-service";
    import { error } from "../../stores/use-error";

    let name: string = "";
    let description: string = "";

    $: {
        name;
        description;
        error.reset();
    }

    async function submit() {
        try {
            await createMeeting(name, description);
            push("/");
        } catch (e) {
            error.set(e);
        }
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
    <div style="color: red;">{$error}</div>
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
