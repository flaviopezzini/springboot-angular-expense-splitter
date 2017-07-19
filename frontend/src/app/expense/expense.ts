export class Expense {
  constructor(public id: string,
              public dateTime: Date,
              public description: string,
              public amount: number,
              public comment: string,
              public userId: string) {

  }
}
