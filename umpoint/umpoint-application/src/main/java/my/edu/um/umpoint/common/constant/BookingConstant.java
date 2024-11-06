package my.edu.um.umpoint.common.constant;

import lombok.Getter;

public interface BookingConstant {
    @Getter
    enum BookingStatus {
        PENDING(0),
        REJECT(1),
        APPROVED(2),
        COMPLETED(3),
        CANCELLED(4),;

        private final int value;

        BookingStatus(int value) {
            this.value = value;
        }
    }

    @Getter
    enum EventStatus {
        BOOKING(0),
        CLOSURE(1);

        private final int value;

        EventStatus(int value) {
            this.value = value;
        }
    }

    @Getter
    enum PaymentStatus {
        PENDING(0),
        SUCCESS(1),
        FAILED(2),
        REFUNDED(3);

        private final int value;

        PaymentStatus(int value) {
            this.value = value;
        }
    }

    @Getter
    enum BookingUnit {
        FREEFORM(0),
        SLOTTED(1);

        private final int value;

        BookingUnit(int value) {
            this.value = value;
        }
    }
}
