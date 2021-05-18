import {HeistSkill} from "./heist-skill";

export class Heist {

  name!: string;
  location!: string;
  startTime!: Date;
  endTime!: Date;
  skills!: HeistSkill[];
  status!: string;

}
