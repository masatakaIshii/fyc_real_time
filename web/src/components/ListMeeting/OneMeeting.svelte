<script type="ts">
    import type { DtoMeeting } from "../../types/meeting";

    export let meeting: DtoMeeting;
</script>

<div class="one-meeting">
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
        &__label {
            font-weight: bold;
        }
    }
</style>
