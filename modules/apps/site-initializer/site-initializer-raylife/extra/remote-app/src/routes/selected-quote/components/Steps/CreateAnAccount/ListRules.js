import {RuleIcon} from '../CreateAnAccount/RuleIcon';

export function ListRules({objValidate}) {
	return (
		<div className="mt-3">
			<ul className="list-unstyled">
				<li>
					<RuleIcon
						label={
							<>
								At least <b>8 characters</b>
							</>
						}
						status={objValidate.qtdCharacter}
					/>
				</li>

				<li>
					<RuleIcon
						label={
							<>
								At least <b>5 unique characters</b>
							</>
						}
						status={objValidate.uniqueCharacter}
					/>
				</li>

				<li>
					<RuleIcon
						label={
							<>
								At least <b>1 symbol</b>
							</>
						}
						status={objValidate.symbolCharacter}
					/>
				</li>

				<li>
					<RuleIcon
						label={
							<>
								At least <b>1 number</b>
							</>
						}
						status={objValidate.numberCharacter}
					/>
				</li>

				<li>
					<RuleIcon
						label={
							<>
								<b>No spaces</b> allowed
							</>
						}
						status={objValidate.noSpace}
					/>
				</li>

				<li>
					<RuleIcon
						label="Passwords are the same"
						status={objValidate.samePassword}
					/>
				</li>
			</ul>
		</div>
	);
}
