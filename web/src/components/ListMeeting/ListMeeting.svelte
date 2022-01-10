<script lang="ts">
    import { onMount } from "svelte";
    import { push } from "svelte-spa-router";
    import { faPlusSquare } from "@fortawesome/free-solid-svg-icons";
    import { FontAwesomeIcon } from 'fontawesome-svelte';
    import OneMeeting from "./OneMeeting.svelte";
    import type { DtoMeeting } from "../../types/meeting";
    import { getAllMeetings } from "../../api/meeting/meeting-service";
import { isAdmin } from "../../api/auth/auth-service";

    let listMeetings: DtoMeeting[] = [];

    onMount(async () => {
        listMeetings = await getAllMeetings();
    });

</script>

<div>
    <h1 class="title">
        <div />
        <div>List Meeting</div>
        {#if isAdmin()}
            <div class="icon" on:click={() => push("/meeting-form")}>
                <FontAwesomeIcon icon={faPlusSquare} />
            </div>
        {/if}
    </h1>
    <div class="content">
        {#each listMeetings as meeting}
            <OneMeeting {meeting} />
        {/each}
    </div>
</div>

<style lang="scss">
    .content {
        padding: 0 2em;
    }

    .title {
        display: flex;
        justify-content: center;
        align-items: center;
        .icon {
            margin-left: 0.5em;
        }

        .icon:hover {
            color: #ff3e00;
            cursor: pointer;
        }
    }
</style>
