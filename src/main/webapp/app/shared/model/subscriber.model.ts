export interface ISubscriber {
    id?: number;
    imsi?: string;
    msisdn?: string;
    nam?: number;
    state?: number;
    category?: number;
    subscriberChargingCharacteristics?: number;
    iccid?: string;
}

export class Subscriber implements ISubscriber {
    constructor(
        public id?: number,
        public imsi?: string,
        public msisdn?: string,
        public nam?: number,
        public state?: number,
        public category?: number,
        public subscriberChargingCharacteristics?: number,
        public iccid?: string
    ) {}
}
