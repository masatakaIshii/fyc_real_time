<script lang="ts">
    import { onMount } from "svelte";
    import { push } from "svelte-spa-router";
    import { faPlusSquare } from "@fortawesome/free-solid-svg-icons";
    import { FontAwesomeIcon } from 'fontawesome-svelte';
    import OneMeeting from "./OneMeeting.svelte";
    import { getAllMeetings } from "../../api/meeting/meeting-service";
    import { isAdmin } from "../../api/auth/auth-service";
    import {listMeeting} from "../../stores/use-list-meeting"


    onMount(async () => {
        const value = await getAllMeetings();
        listMeeting.set(value)
    });

</script>

<div>
    <h1 class="title">
        <div>List Meeting</div>
        {#if isAdmin()}
            <div class="icon" on:click={() => push("/meeting-form")}>
                <FontAwesomeIcon icon={faPlusSquare} />
            </div>
        {/if}
    </h1>
    <div class="content">
        {#each $listMeeting as meeting}
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
