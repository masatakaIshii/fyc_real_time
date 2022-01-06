<script lang="ts">
    import { onMount } from "svelte";
    import { push } from "svelte-spa-router";
    import { faPlusSquare } from "@fortawesome/free-solid-svg-icons";
    import { FontAwesomeIcon } from 'fontawesome-svelte';
    import { isLoggedIn } from "../../stores/use-is-logged-in";
    import OneMeeting from "./OneMeeting.svelte";
    import type { DtoMeeting } from "../../types/meeting";
    import { getAllMeetings } from "../../api/meeting/meeting-service";

    let listMeetings: DtoMeeting[] = [];

    onMount(async () => {
        if ($isLoggedIn === false) {
            push("/home");
            return;
        }
        listMeetings = await getAllMeetings();
    });

</script>

<div>
    <h1 class="title">
        <div />
        <div>List Meeting</div>
        <div class="icon" on:click={() => push("/meeting/create")}>
            <FontAwesomeIcon icon={faPlusSquare} />
        </div>
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
