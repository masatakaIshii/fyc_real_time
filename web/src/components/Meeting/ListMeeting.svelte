<script lang="ts">
    import { onMount } from "svelte";
    import { getAllMeetings } from "../../api/meeting/meeting-service";
    import type { DtoMeeting } from "../../types/meeting";
    import OneMeeting from "./OneMeeting.svelte";

    let listMeetings: DtoMeeting[] = [];

    onMount(async () => {
        listMeetings = await getAllMeetings();
    });
</script>

<div>
    <h1>List Meeting</h1>
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
</style>
