<script type="ts">
    import { getAuthUsername } from "../../api/auth/auth-service";
    import { deleteMeeting } from "../../api/meeting/meeting-service";
    import { listMeeting } from "../../stores/use-list-meeting";

    import type { DtoMeeting } from "../../types/meeting";

    export let meeting: DtoMeeting;

    let authUsername = getAuthUsername();
    let isCreator = meeting.creator.email === authUsername;

    function updateOne() {
        console.log("update meeting");
    }

    async function deleteOne() {
        try {
            await deleteMeeting(meeting.id);
            listMeeting.removeOne(meeting);
        } catch (err) {
            console.error(err);
        }
    }
</script>

<div class={isCreator ? "one-meeting creator" : "one-meeting"}>
    <div class="one-meeting__group">
        <div class="one-meeting__label">Meeting name:</div>
        <div>{meeting.name}</div>
    </div>
    <div class="one-meeting__group">
        <div class="one-meeting__label">Date:</div>
        <div>{meeting.createdDateTime}</div>
    </div>
    <div class="one-meeting__group">
        <div class="one-meeting__label">Creator name:</div>
        <div>{meeting.creator.name}</div>
    </div>
    <div class="one-meeting__group">
        <div class="one-meeting__label">State meeting</div>
        <div>{meeting.isClosed ? "Closed" : "Open"}</div>
    </div>
    {#if isCreator}
        <div class="one-meeting__group-btns">
            <button class="update-btn" on:click={updateOne}>Update</button>
            <button class="delete-btn" on:click={deleteOne}>Delete</button>
        </div>
    {/if}
</div>

<style lang="scss">
    .one-meeting {
        padding: 1em;
        border: 0.1rem solid gray;
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        grid-gap: 10px;
        grid-auto-rows: minmax(60px, auto);
        &__group {
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        &__group-btns {
            display: flex;
            flex-direction: column;
            justify-content: center;
            padding: 20px;
        }

        &__label {
            font-weight: bold;
        }
    }

    .creator {
        grid-template-columns: repeat(5, 1fr);
    }
</style>
